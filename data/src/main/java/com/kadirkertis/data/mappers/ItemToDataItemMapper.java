package com.kadirkertis.data.mappers;

import com.kadirkertis.data.model.DataItem;
import com.kadirkertis.domain.interactor.product.model.Item;

import java.util.List;

/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public interface ItemToDataItemMapper {

    DataItem map(Item item);
    List<DataItem> mapList(List<Item> items);
}
