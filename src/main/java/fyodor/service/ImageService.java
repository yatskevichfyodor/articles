package fyodor.service;

import fyodor.model.Image;
import fyodor.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService implements IImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image save(String file) {
        Image image = new Image();
        image.setData(file);
        return imageRepository.save(image);
    }

    @Transactional
    @Override
    public Image saveOnServer(MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            String uploadsDir = "/uploads/images";
            String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
            if (!new File(realPathtoUploads).exists()) {
                new File(realPathtoUploads).mkdir();
            }

            Image image = new Image();
            image = imageRepository.save(image);

            String orgName = image.getId().toString() + "-" + file.getOriginalFilename();
            String filePath = realPathtoUploads + orgName;
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            image.setData(orgName);
            return imageRepository.save(image);
        }
        return null;
    }
}
