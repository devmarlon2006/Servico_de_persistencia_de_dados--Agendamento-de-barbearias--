package br.com.devmarlon2006.registrationbarberservice.Service.model;

import lombok.Data;

import java.util.List;


@Data
public class DataTransferObject {
    private List<Object> colectionData;

    public Object requiredType(Class<?> type) {
        for (Object colectionDatum : colectionData) {
            if (colectionDatum.getClass().equals( type )) {
                return colectionDatum;
            }
        }
        return null;
    }
}
