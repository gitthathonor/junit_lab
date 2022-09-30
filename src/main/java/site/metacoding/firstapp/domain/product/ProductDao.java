package site.metacoding.firstapp.domain.product;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDao {
    public Product findById(Integer productId);

    public List<Product> findAll();

    public int insert(Product product);

    public int update(Product product);

    public int deleteById(Integer productId);
}
