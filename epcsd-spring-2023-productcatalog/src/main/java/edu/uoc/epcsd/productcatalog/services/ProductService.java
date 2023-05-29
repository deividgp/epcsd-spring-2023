package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.entities.Item;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private ItemService itemService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public void deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if(!product.isPresent()) return;

        Product auxProduct = product.get();
        List<Item> items = auxProduct.getItemList();

        // Deleting the associated items one by one because I am not sure if I am allowed to modify the Product entity to include CascadeType.REMOVE
        for (Item auxItem : items){
            itemService.delete(auxItem);
        }

        productRepository.deleteById(productId);
    }

    public Product createProduct(Long categoryId, String name, String description, Double dailyPrice, String brand, String model) {

        Product product = Product.builder().name(name).description(description).dailyPrice(dailyPrice).brand(brand).model(model).build();

        if (categoryId != null) {
            Optional<Category> category = categoryService.findById(categoryId);

            if (category.isPresent()) {
                product.setCategory(category.get());
            }
        }

        return productRepository.save(product);
    }

    public List<Product> findByName(String name) {
        return productRepository.findProductsByName(name);
    }

    public List<Product> findAllByCategory(Long categoryId) {
        return productRepository.findProductsByCategory_Id(categoryId);
    }
}
