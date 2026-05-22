package com.app.demo.domain.service;

import com.app.demo.domain.dto.RecomendacionDTO;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.entity.ValoracionPlan;
import com.app.demo.persistence.repository.PlanEmpresaRepository;
import com.app.demo.persistence.repository.ValoracionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    private static final int TOP_N = 6;

    @Autowired
    private ValoracionPlanRepository valoracionRepo;

    @Autowired
    private PlanEmpresaRepository planRepo;

    public List<RecomendacionDTO> recomendar(Long clienteId) {
        List<ValoracionPlan> todasValoraciones = valoracionRepo.findAll();

        // Matrices: userId→{planId→rating} y planId→{userId}
        Map<Long, Map<Long, Integer>> usuarioPlanes = new HashMap<>();
        Map<Long, Set<Long>> planUsuarios = new HashMap<>();

        for (ValoracionPlan v : todasValoraciones) {
            Long uid = v.getCliente().getIdCliente();
            Long pid = v.getPlanEmpresa().getId();
            usuarioPlanes.computeIfAbsent(uid, k -> new HashMap<>()).put(pid, v.getPuntuacion());
            planUsuarios.computeIfAbsent(pid, k -> new HashSet<>()).add(uid);
        }

        Map<Long, Integer> planesDelUsuario = usuarioPlanes.getOrDefault(clienteId, new HashMap<>());

        // Sin historial → top populares (arranque en frío)
        if (planesDelUsuario.isEmpty()) {
            return planRepo.findByDisponibleTrue().stream()
                    .filter(p -> p.getValoracionPromedio() != null)
                    .sorted(Comparator.comparingDouble(PlanEmpresa::getValoracionPromedio).reversed())
                    .limit(TOP_N)
                    .map(p -> toDTO(p, "popular", p.getValoracionPromedio()))
                    .collect(Collectors.toList());
        }

        Set<Long> planesVistos = planesDelUsuario.keySet();

        // Encontrar usuarios similares con similitud coseno
        Map<Long, Double> similitudUsuarios = new HashMap<>();
        for (Long planId : planesVistos) {
            for (Long otroUid : planUsuarios.getOrDefault(planId, new HashSet<>())) {
                if (otroUid.equals(clienteId)) continue;
                double sim = similitudCoseno(planesDelUsuario, usuarioPlanes.get(otroUid));
                similitudUsuarios.merge(otroUid, sim, Double::sum);
            }
        }

        // Puntuar planes candidatos (filtrado colaborativo)
        Map<Long, Double> scoreAcum = new HashMap<>();
        Map<Long, Double> pesoAcum = new HashMap<>();

        for (Map.Entry<Long, Double> simEntry : similitudUsuarios.entrySet()) {
            double sim = simEntry.getValue();
            if (sim <= 0) continue;
            Map<Long, Integer> otroPlanes = usuarioPlanes.get(simEntry.getKey());
            for (Map.Entry<Long, Integer> pe : otroPlanes.entrySet()) {
                Long pid = pe.getKey();
                if (planesVistos.contains(pid)) continue;
                scoreAcum.merge(pid, sim * pe.getValue(), Double::sum);
                pesoAcum.merge(pid, sim, Double::sum);
            }
        }

        // Normalizar scores
        List<RecomendacionDTO> resultado = new ArrayList<>();
        scoreAcum.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(TOP_N)
                .forEach(e -> planRepo.findById(e.getKey()).ifPresent(plan -> {
                    if (plan.isDisponible()) {
                        double score = e.getValue() / pesoAcum.getOrDefault(e.getKey(), 1.0);
                        resultado.add(toDTO(plan, "colaborativo", Math.round(score * 100.0) / 100.0));
                    }
                }));

        // Completar con filtrado por contenido (mismo tipoSitio favorito)
        if (resultado.size() < TOP_N) {
            String tipoFav = tipoFavorito(planesDelUsuario);
            Set<Long> yaIncluidos = resultado.stream()
                    .map(RecomendacionDTO::getPlanId).collect(Collectors.toSet());
            yaIncluidos.addAll(planesVistos);

            planRepo.findByTipoSitioIgnoreCaseAndDisponibleTrue(tipoFav).stream()
                    .filter(p -> !yaIncluidos.contains(p.getId()))
                    .sorted(Comparator.comparingDouble((PlanEmpresa p) ->
                            p.getValoracionPromedio() != null ? -p.getValoracionPromedio() : 0.0))
                    .limit(TOP_N - resultado.size())
                    .forEach(p -> resultado.add(toDTO(p, "contenido",
                            p.getValoracionPromedio() != null ? p.getValoracionPromedio() : 0.0)));
        }

        return resultado;
    }

    private double similitudCoseno(Map<Long, Integer> u1, Map<Long, Integer> u2) {
        if (u2 == null) return 0.0;
        Set<Long> comunes = new HashSet<>(u1.keySet());
        comunes.retainAll(u2.keySet());
        if (comunes.isEmpty()) return 0.0;

        double dot = 0, n1 = 0, n2 = 0;
        for (Long pid : comunes) dot += u1.get(pid) * u2.get(pid);
        for (int r : u1.values()) n1 += r * r;
        for (int r : u2.values()) n2 += r * r;
        return (n1 == 0 || n2 == 0) ? 0.0 : dot / (Math.sqrt(n1) * Math.sqrt(n2));
    }

    private String tipoFavorito(Map<Long, Integer> planesDelUsuario) {
        Map<String, Double> tipoScore = new HashMap<>();
        for (Map.Entry<Long, Integer> e : planesDelUsuario.entrySet()) {
            planRepo.findById(e.getKey()).ifPresent(p ->
                    tipoScore.merge(p.getTipoSitio(), (double) e.getValue(), Double::sum));
        }
        return tipoScore.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private RecomendacionDTO toDTO(PlanEmpresa plan, String metodo, double score) {
        return RecomendacionDTO.builder()
                .planId(plan.getId())
                .nombre(plan.getNombre())
                .tipoSitio(plan.getTipoSitio())
                .ciudad(plan.getCiudad())
                .precio(plan.getPrecio())
                .valoracionPromedio(plan.getValoracionPromedio())
                .imagenUrl(plan.getImagen())
                .metodoRecomendacion(metodo)
                .scoreRelevancia(score)
                .build();
    }
}
