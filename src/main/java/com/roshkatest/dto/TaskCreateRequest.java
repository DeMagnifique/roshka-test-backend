package com.roshkatest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Data
public class TaskCreateRequest {
    
    @NotBlank(message = "El titulo es obligatorio")
    @Length(max = 100, message = "El titulo no puede exceder 100 caracteres")
    private String title;

    @Length(max = 500, message = "La descripcion no puede exceder 500 caracteres")
    private String description;

    @Length(max = 50, message = "La categoria no puede exceder 50 caracteres")
    private String category;
}
