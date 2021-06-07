package com.service.impl;

import com.service.BookTitleService;
import com.service.UploadImageUtils;
import com.domain.BookTitle;
import com.repository.BookTitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BookTitle}.
 */
@Service
@Transactional
public class BookTitleServiceImpl implements BookTitleService {

    private final Logger log = LoggerFactory.getLogger(BookTitleServiceImpl.class);

    @Value("${folder.upload.url}")
    private String folderNameMain;
    
    @Value("${folder.upload.urlTarget}")
    private String folderNameTarget;

    private final BookTitleRepository bookTitleRepository;

    public BookTitleServiceImpl(BookTitleRepository bookTitleRepository) {
        this.bookTitleRepository = bookTitleRepository;
    }

    @Override
    public BookTitle save(BookTitle bookTitle) {
        return bookTitleRepository.save(bookTitle);
    }

    @Override
    public BookTitle save(BookTitle bookTitle, MultipartFile file) {
        try {
            log.debug("Request to save BookTitle : {}", bookTitle);

            UploadImageUtils uploadImageUtils = new UploadImageUtils();

            BookTitle maxId = bookTitleRepository.findByMaxId();
            String fileName = "";

            if (maxId == null) {
                fileName = uploadImageUtils.setNameImage(file.getOriginalFilename(), 0L);
                bookTitle.setImage(fileName);
            } else {
                fileName = uploadImageUtils.setNameImage(file.getOriginalFilename(), maxId.getId());
            }

            bookTitle.setDateCreate(LocalDate.now());
            bookTitle.setDateUpdate(LocalDate.now());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            bookTitle.setUserCreate(authentication.getName());
            bookTitle.setImage(fileName);

            Path rootMain = Paths.get(folderNameMain);
            uploadImageUtils.save(file, fileName, rootMain);
            
            Path rootTarget = Paths.get(folderNameTarget);
            uploadImageUtils.save(file, fileName, rootTarget);

            return bookTitleRepository.save(bookTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BookTitle update(BookTitle bookTitle, MultipartFile file) {
        try{
            Path rootMain = Paths.get(folderNameMain);
            Path rootTarget = Paths.get(folderNameTarget);

            Optional<BookTitle> bookTitleOptional = bookTitleRepository.findById(bookTitle.getId());

            if(bookTitleOptional.isPresent()) {
                bookTitle.setDateCreate(bookTitleOptional.get().getDateCreate());

                String fileName = bookTitleOptional.get().getImage();

                bookTitle.setDateUpdate(LocalDate.now());
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                bookTitle.setUserCreate(authentication.getName());
                bookTitle.setImage(fileName);

                UploadImageUtils uploadImageUtils = new UploadImageUtils();
                uploadImageUtils.delete(rootMain, fileName);
                uploadImageUtils.delete(rootTarget, fileName);

                uploadImageUtils.save(file, fileName, rootMain);
                uploadImageUtils.save(file, fileName, rootTarget);

                return bookTitleRepository.save(bookTitle);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookTitle> findAll(Pageable pageable) {
        log.debug("Request to get all BookTitles");
        return bookTitleRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BookTitle> findOne(Long id) {
        log.debug("Request to get BookTitle : {}", id);
        return bookTitleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookTitle : {}", id);
        Path rootMain = Paths.get(folderNameMain);
        Path rootTarget = Paths.get(folderNameTarget);

        Optional<BookTitle> bookTitleOptional = bookTitleRepository.findById(id);

        if(bookTitleOptional.isPresent()) {
            UploadImageUtils uploadImageUtils = new UploadImageUtils();
            uploadImageUtils.delete(rootMain, bookTitleOptional.get().getImage());
            uploadImageUtils.delete(rootTarget, bookTitleOptional.get().getImage());
            
            bookTitleRepository.deleteById(id);
        }
    }
}
