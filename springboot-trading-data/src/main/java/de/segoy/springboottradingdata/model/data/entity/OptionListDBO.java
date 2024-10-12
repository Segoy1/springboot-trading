package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionListDBO extends IBKRDataType {

    @Id
    private Long id;
    
    @OneToMany
    private List<OptionMarketDataDBO> optionList;
}
