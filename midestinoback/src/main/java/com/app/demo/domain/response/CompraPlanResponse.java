package com.app.demo.domain.response;

import com.app.demo.domain.dto.CompraPlanDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraPlanResponse {

    private String message;
    private boolean valid;
    private CompraPlanDTO compraPlan;
    private List<CompraPlanDTO> compraPlanList;
}
