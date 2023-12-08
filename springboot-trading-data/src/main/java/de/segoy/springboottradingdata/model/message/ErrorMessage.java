package de.segoy.springboottradingdata.model.message;

import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage extends IBKRDataTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer errorId;

    @NotBlank
    @Lob
    private String message;

    @CreationTimestamp
    private Timestamp createDate;
}