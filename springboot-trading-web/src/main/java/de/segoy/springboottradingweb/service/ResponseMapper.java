package de.segoy.springboottradingweb.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseMapper {

    public <T> ResponseEntity<T> mapResponse(Optional<T> responseObjectOptional){
        return responseObjectOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    public <T> ResponseEntity<List<T>> mapResponse(List<T> responseObjectList){
        return responseObjectList.isEmpty()?ResponseEntity.badRequest().build():ResponseEntity.ok(responseObjectList);
    }
    public <T> ResponseEntity<T> mapResponse(T responseObject){
        return ResponseEntity.ok(responseObject);
    }
}
