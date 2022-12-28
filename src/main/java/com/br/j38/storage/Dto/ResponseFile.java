package com.br.j38.storage.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {

    private String name;
    private String type;
    private String url;
    private long size;
}
