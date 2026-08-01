package nechto.mappers;

import nechto.dto.TableDto;
import nechto.entity.Table;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    TableDto convertToTableDto(Table table);

    Table convertToTable(TableDto table);

    List<TableDto> convertToListResponseTableDto(List<Table> tables);

}
