package com.westas.orderassembly;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.random;

public class ListSubdivision {
    public ListSubdivision()
    {
        this.list = new ArrayList<>();


        Subdivision subdivision = new Subdivision();
        subdivision.Name = "Кофейня на Рождественке";
        subdivision.Number = 101;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Кофейня на Никитской";
        subdivision.Number = 104;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Четыре Ветра";
        subdivision.Number = 111;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Кофейня на Кудринской";
        subdivision.Number = 130;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Неглинная";
        subdivision.Number = 177;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Осенняя";
        subdivision.Number = 180;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Депо Бар";
        subdivision.Number =  191;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Аврора";
        subdivision.Number = 205;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Шереметьево-1";
        subdivision.Number = 212;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Бургерная";
        subdivision.Number = 255;
        this.list.add(subdivision);

        subdivision = new Subdivision();
        subdivision.Name = "Мэрия";
        subdivision.Number = 264;
        this.list.add(subdivision);
    }

    public java.util.List<Subdivision> list;
}
