package com.lodoss.examples.rx;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by user on 31/01/17.
 */

public class ScanVsReduceExample {

    public static void main(String... args) {
        Observable.range(0, 10).reduce(new ArrayList<>(), (list, i) -> {
            list.add(i);
            return list;
        }).subscribe(System.out::println);

        System.out.println("... vs ...");

        Observable.range(0, 10).scan(new ArrayList<>(), (list, i) -> {
            list.add(i);
            return list;
        }).forEach(System.out::println);
    }
}
