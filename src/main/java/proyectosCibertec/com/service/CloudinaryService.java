package proyectosCibertec.com.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String subirImagen(MultipartFile file, String folder) {
        try {
            @SuppressWarnings("unchecked")
			Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "folder", folder // Especificamos la carpeta
            );
            
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            
            return result.get("secure_url").toString();
            
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }

    public void eliminarImagen(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
        }
    }
}
