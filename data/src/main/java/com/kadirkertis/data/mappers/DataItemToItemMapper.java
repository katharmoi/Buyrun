package com.kadirkertis.data.mappers;

import com.kadirkertis.data.model.DataItem;
import com.kadirkertis.domain.interactor.product.model.Item;

import java.util.List;

/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public interface DataItemToItemMapper {
    Item map(DataItem dataItem);
    List<Item> mapList(List<DataItem> dataItems);
}
