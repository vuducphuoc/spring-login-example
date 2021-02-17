/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuoc.vd.springloginexample.payload;

import lombok.*;
import lombok.experimental.Accessors;
import phuoc.vd.springloginexample.exception.StatusResponse;

import java.io.Serializable;

/**
 *
 * @author abc
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GenericResponse<T> implements Serializable {

    Integer status;
    String message;
    String description;
    StatusResponse apiStatus;
    T data;

    public GenericResponse<T> setApiStatus(StatusResponse apiStatus){
        this.status = apiStatus.getCode();
        this.message = apiStatus.getMessage();
        this.description = apiStatus.getDescription();
        this.apiStatus = apiStatus;
        return this;
    }
    public StatusResponse getApiStatus(){return this.apiStatus;}
}
