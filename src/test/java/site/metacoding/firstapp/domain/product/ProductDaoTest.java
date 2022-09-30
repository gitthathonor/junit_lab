package site.metacoding.firstapp.domain.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import site.metacoding.firstapp.config.MyBatisConfig;

@Import(MyBatisConfig.class) // MyBatisTest가 MyBatisConfig를 못읽음, 아마 실제 회사에서는 이렇게까지 강제로 할 필요는 없을듯
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실DB사용, 원래는 임베디드 DB가 열린다.
@MybatisTest
// @SpringBootTest
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void findById_test() {
        // given(받아야 할 것)
        Integer productId = 1;

        // when(테스트)
        Product productPS = productDao.findById(productId);

        // then(검증)
        assertEquals("바나나", productPS.getProductName());
    }

    @Test
    public void findAll_test() {
        // given

        // when
        List<Product> productListPS = productDao.findAll();
        System.out.println(productListPS.get(0).getProductName());

        // then
        assertEquals(2, productListPS.size());
    }

    // JUnit은 메서드 실행직전에 트랜잭션이 걸리고, 메서드 실행이 끝나면 rollback 됨.
    // MyBatis는 ResultSet을 자바 Entity로 변경해줄 때, 빈생성자를 호출하고 setter가 없어도 값을 매핑해준다.
    @Test
    public void insert_test() {
        // given
        String productName = "수박";
        Integer productPrice = 1000;
        Integer productQty = 100;

        Product product = new Product(productName, productPrice, productQty); // 내가 만든 것이기 때문에 PS를 붙이면 안 된다.

        // when
        int result = productDao.insert(product);

        // then
        assertEquals(1, result);

    } // rollback

    // 두 개의 메서드를 하나의 트랜잭션으로 묶거나 해야할 때는 따로 처리를 해주는 방법이 있다.

    @Test
    public void update_test() {
        // given
        Integer productId = 1;
        String productName = "수박";
        Integer productPrice = 1000;
        Integer productQty = 100;

        Product product = new Product(productName, productPrice, productQty);
        product.setProductId(productId);

        // verify
        Product productPS = productDao.findById(product.getProductId());
        assertTrue(productPS == null ? false : true);

        // when
        // product [id=1, productName="수박", productPrice=1000, productQty=100]
        // productPS [id=1, productName="바나나", productPrice=3000, productQty=98,
        // createdAt=2022-09-29]
        productPS.update(product);
        // productPS [id=1, productName="수박", productPrice=1000, productQty=100]
        int result = productDao.update(productPS);

        // then
        assertEquals(1, result);

    } // rollback

    @Test
    public void deleteById_test() {
        // given
        Integer productId = 1;

        // verify
        Product productPS = productDao.findById(productId);
        assertTrue(productPS == null ? false : true);

        // when
        int result = productDao.deleteById(productPS.getProductId());

        // then
        assertEquals(1, result);
    }

}
