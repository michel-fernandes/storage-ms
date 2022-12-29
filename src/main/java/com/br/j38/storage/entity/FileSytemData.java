package com.br.j38.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_STORAGE_REF")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileSytemData implements Serializable {
    private static final  long serialVersionUID= 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {@Parameter(
            name = "uuid_gen_strategy_class",
            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
        )}
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String type;
    private String filePath;

}