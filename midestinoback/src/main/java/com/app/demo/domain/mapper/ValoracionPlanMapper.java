package com.app.demo.domain.mapper;

import org.springframework.stereotype.Component;

import com.app.demo.domain.dto.ValoracionPlanDTO;
import com.app.demo.persistence.entity.ValoracionPlan;

@Component
public class ValoracionPlanMapper {

    public static ValoracionPlanDTO toDto(ValoracionPlan entity) {
        if (entity == null) return null;
    return ValoracionPlanDTO.builder()
        .id(entity.getId())
        .planEmpresaId(entity.getPlanEmpresa() != null ? entity.getPlanEmpresa().getId() : null)
        .clienteId(entity.getCliente() != null ? entity.getCliente().getIdCliente() : null)
        .puntuacion(entity.getPuntuacion())
        .comentario(entity.getComentario())
        .fecha(entity.getFecha())
        .build();
    }
}
