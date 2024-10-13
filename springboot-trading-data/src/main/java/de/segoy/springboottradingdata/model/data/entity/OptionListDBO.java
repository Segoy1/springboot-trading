package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
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
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="OPTION_LIST")
    private List<OptionMarketDataDBO> optionList;
}
