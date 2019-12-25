package com.westas.orderassembly;

import java.util.ArrayList;
import java.util.List;

public class ListInvoiceItem {
    public ListInvoiceItem()
    {
        list = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4523345";
        invoiceItem.Name = "Пирог Черри Банана 180г";
        invoiceItem.RequiredQuantity = 23;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "452312";
        invoiceItem.Name = "Кофе КАПУЧИНО 150мл";
        invoiceItem.RequiredQuantity = 78;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "822174";
        invoiceItem.Name = "Цыплёнок на даче 210г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "2343698";
        invoiceItem.Name = "Салат Капуста кимчи 200г";
        invoiceItem.RequiredQuantity = 765;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "460712598320";
        invoiceItem.Name = "Маслины вяленые п/ф";
        invoiceItem.RequiredQuantity = 6;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "76543698";
        invoiceItem.Name = "Тар-тар из говядины 210г";
        invoiceItem.RequiredQuantity = 234;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "45239876";
        invoiceItem.Name = "Сэт мини бургеров 800г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "1243698";
        invoiceItem.Name = "Гранд бургер 550г";
        invoiceItem.RequiredQuantity = 67.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4523698";
        invoiceItem.Name = "Котлета д/бургера 90г";
        invoiceItem.RequiredQuantity = 40.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4523698";
        invoiceItem.Name = "Пожарская котлета 240г";
        invoiceItem.RequiredQuantity = 55.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "5523698";
        invoiceItem.Name = "Бургер Крабс 250г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4523698";
        invoiceItem.Name = "Молоко сгущенное 80г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "7623698";
        invoiceItem.Name = "Наггетсы сырные 170г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "452312";
        invoiceItem.Name = "Кофе КАПУЧИНО 150мл";
        invoiceItem.RequiredQuantity = 78;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "822174";
        invoiceItem.Name = "Цыплёнок на даче 210г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "2343698";
        invoiceItem.Name = "Салат Капуста кимчи 200г";
        invoiceItem.RequiredQuantity = 765;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "8763698";
        invoiceItem.Name = "Маслины вяленые п/ф";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "76543698";
        invoiceItem.Name = "Тар-тар из говядины 210г";
        invoiceItem.RequiredQuantity = 234;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "45239876";
        invoiceItem.Name = "Сэт мини бургеров 800г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "1243698";
        invoiceItem.Name = "Гранд бургер 550г";
        invoiceItem.RequiredQuantity = 67.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4527773698";
        invoiceItem.Name = "Котлета д/бургера 90г";
        invoiceItem.RequiredQuantity = 40.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "460712598322";
        invoiceItem.Name = "Пожарская котлета 240г";
        invoiceItem.RequiredQuantity = 5.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "460712598";
        invoiceItem.Name = "Бургер Крабс 250г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "4523698";
        invoiceItem.Name = "Молоко сгущенное 80г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
        invoiceItem = new InvoiceItem();
        invoiceItem.Barcode = "7623698";
        invoiceItem.Name = "Наггетсы сырные 170г";
        invoiceItem.RequiredQuantity = 0.4;
        invoiceItem.Unit = "кг.";

        list.add(invoiceItem);
    }

    private List<InvoiceItem> list;

    public InvoiceItem GetItems(int position)
    {
        return list.get(position);
    }

    public int GetSize()
    {
        return list.size();
    }

    public boolean VerifyGoods(double quantity, String code)
    {
        int position = 0;
        for (InvoiceItem itm: list)
        {
            if (itm.Barcode.equals(code))
            {
                itm.Quantity += quantity;

                if (itm.Quantity > itm.RequiredQuantity)
                {
                    itm.Verify = Satus.over;
                }
                if (itm.Quantity == itm.RequiredQuantity)
                {
                    itm.Verify = Satus.equally;
                }
                if (itm.Quantity < itm.RequiredQuantity)
                {
                    itm.Verify = Satus.less;
                }


                //Меняем позицию найденного item на первую
                if(position != 0)
                {
                    list.remove(position);
                    list.add(0,itm);
                }

                return true;
            }
            position++;
        }

        return false;
    }

    public void ChangeQuantity(double quantity, String code)
    {
        int position = 0;
        for (InvoiceItem itm: list)
        {
            if (itm.Barcode.equals(code))
            {
                itm.Quantity = quantity;

                if (itm.Quantity > itm.RequiredQuantity)
                {
                    itm.Verify = Satus.over;
                }
                if (itm.Quantity == itm.RequiredQuantity)
                {
                    itm.Verify = Satus.equally;
                }
                if (itm.Quantity < itm.RequiredQuantity)
                {
                    itm.Verify = Satus.less;
                }


                //Меняем позицию найденного item на первую
                if(position != 0)
                {
                    list.remove(position);
                    list.add(0,itm);
                }

                break;

            }
            position++;
        }
    }

}
