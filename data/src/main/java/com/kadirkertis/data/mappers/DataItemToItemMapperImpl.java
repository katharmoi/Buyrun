package com.kadirkertis.data.mappers;

import com.annimon.stream.Stream;
import com.kadirkertis.data.model.DataItem;
import com.kadirkertis.domain.interactor.product.model.Item;

import java.util.List;

/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public class DataItemToItemMapperImpl implements DataItemToItemMapper {
    @Override
    public Item map(DataItem dataItem) {
        return new Item(dataItem.getKey(), dataItem.getCategory(), dataItem.getSubCategory(), dataItem.getName(),
                dataItem.getDescription(), dataItem.getUrl(), dataItem.getPrice(), dataItem.getTimeAddedLong(), dataItem.getTimeLastEditedLong());
    }

    @Override
    public List<Item> mapList(List<DataItem> dataItems) {
        return Stream.of(dataItems)
                .map(this::map)
                .toList();
    }
}
