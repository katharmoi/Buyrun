package com.kadirkertis.data.mappers;

import com.annimon.stream.Stream;
import com.kadirkertis.data.model.DataItem;
import com.kadirkertis.domain.interactor.product.model.Item;

import java.util.List;


/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public class ItemToDataItemMapperImpl implements ItemToDataItemMapper {
    @Override
    public DataItem map(Item item) {
        return new DataItem();
    }

    @Override
    public List<DataItem> mapList(List<Item> items) {
        return Stream.of(items)
                .map(this::map)
                .toList();
    }
}
