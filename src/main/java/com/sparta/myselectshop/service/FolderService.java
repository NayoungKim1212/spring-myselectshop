package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    public void addFolders(List<String> folderNames, User user) { // 폴더 이름들을 기준으로 회원이 이미 생성한 폴더들을 조회

        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames); // In : 이름 여러 개를 찾기 위해

        List<Folder> folderList = new ArrayList<>();

        for (String folderName : folderNames) { // 받아온 폴더 이름이랑 찾아온 existFolderList 항목의 name이랑 같으면 안됨
            if(!isExistFolderName(folderName, existFolderList)) { // 받아온 폴더 이름(folderName)이랑 찾아온 existFolderList 항목의 name이랑 같으면 true 반환
                // isExistFolderName으로 체크했을 때 중복이 되지 않았으면 false, false + ! = true
                Folder folder = new Folder(folderName, user); // 중복되지 않았으므로 저장할 새로운 폴더를 생성
                folderList.add(folder);
            }else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다.");
            }
        }
        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }
        return responseDtoList;
    }

    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            if(folderName.equals(existFolder.getName())) {
                return true;
            }
        }
        return false;
    }


}