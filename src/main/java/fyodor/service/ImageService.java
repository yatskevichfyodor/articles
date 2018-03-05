package fyodor.service;

import fyodor.model.Image;
import fyodor.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements IImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image save(String file) {
        Image image = new Image();
        image.setName("no matter");
        image.setData(file);
        return imageRepository.save(image);
    }
}
