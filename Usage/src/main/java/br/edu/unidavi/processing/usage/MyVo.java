package br.edu.unidavi.processing.usage;

import br.edu.unidavi.processing.annotation.Vo;

/**
 * Created by zozfabio on 28/06/2016.
 */
@Vo
public class MyVo {

    private final String value;

    public MyVo(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
