package fyodor.service;

import fyodor.model.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface IImageService {
    Image save(String image);

    Image saveOnServer(MultipartFile file, HttpServletRequest request);

    void delete(Image image);
}
