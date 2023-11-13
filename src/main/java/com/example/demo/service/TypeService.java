package com.example.demo.service;

import com.example.demo.dto.request.TypeRequest;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.entity.Type;
import com.example.demo.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    public TypeResponse saveType(TypeRequest typeRequest){
        Type type = Type.builder()
                .nameType(typeRequest.getNameType())
                .build();
        Type typed = typeRepository.save(type);
        TypeResponse typeResponse = TypeResponse.builder()
                .id(typed.getId())
                .nameType(typed.getNameType())
                .build();
        return typeResponse;
    }
    public List<TypeResponse> findAll(){
        List<Type> types = typeRepository.findAll();
        List<TypeResponse> typeResponses = new ArrayList<>();
        for(Type type : types){
            TypeResponse typeResponse = TypeResponse.builder()
                    .id(type.getId())
                    .nameType(type.getNameType())
                    .build();
            typeResponses.add(typeResponse);
        }
        return typeResponses;
    }

    @Transactional
    public boolean deleteByTypeId(Long typeId){
        Type type = typeRepository.findById(typeId).orElse(null);
        if(type == null){
            return false;
        }
        else{
            typeRepository.deleteById(typeId);
        }
        return true;
    }

    @Transactional
    public TypeResponse updateById(Long id, String nameType){
        Type type = typeRepository.findById(id).orElse(null);
        if(type == null){
            return null;
        }
        else{
            type.setNameType(nameType);
            typeRepository.save(type);
            TypeResponse typeResponse = TypeResponse.builder()
                    .id(type.getId())
                    .nameType(type.getNameType())
                    .build();
            return typeResponse;
        }

    }
//    public static void main(String[] args) {
//        TypeService typeService = new TypeService();
//        TypeRequest typeRequest = TypeRequest.builder()
//                .id(1L)
//                .nameType("test")
//                .build();
//        TypeResponse typeResponse =typeService.saveType(typeRequest);
//        System.out.println(typeResponse);
//    }

}
