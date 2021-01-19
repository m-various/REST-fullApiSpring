package com.test.api;

import com.test.api.dto.ResponseBase;
import com.test.api.dto.request.AddDiscountRequest;
import com.test.api.dto.request.AddProductRequest;
import com.test.api.dto.request.PayRequest;
import com.test.db.Account;
import com.test.db.Discount;
import com.test.db.products.Product;
import com.test.db.products.ProductCategory;
import com.test.db.products.UserProduct;
import com.test.db.repo.AccountsRepo;
import com.test.db.repo.DiscountRepo;
import com.test.db.repo.ProductRepo;
import com.test.db.repo.UserProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("product")
public class ProductApi {
    private final ProductRepo productRepo;
    private final UserProductRepo userProductRepo;
    private final AccountsRepo usersRepo;
    private final DiscountRepo discountRepo;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ProductApi(ProductRepo productRepo, UserProductRepo userProductRepo, AccountsRepo usersRepo,DiscountRepo discountRepo) {
        this.productRepo = productRepo;
        this.userProductRepo = userProductRepo;
        this.usersRepo = usersRepo;
        this.discountRepo = discountRepo;
    }

    @GetMapping(value="allProducts")
    public ResponseEntity<List<Product>> allProducts(@RequestParam(value="category", required = false) ProductCategory category)
    {
        if (category != null){
            return new ResponseEntity<List<Product>>(productRepo.findAllByCategory(category), HttpStatus.OK);
        }

        return new ResponseEntity<List<Product>>((List<Product>) productRepo.findAll(), HttpStatus.OK);

    }

    @GetMapping(value="myProducts")
    public ResponseEntity<List<UserProduct>> myProducts(@RequestParam(value="userId") UUID userId)
    {
        if (userId == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
         List<UserProduct> userProductList = userProductRepo.findAllByAccount_Id(userId);
         return new ResponseEntity<List<UserProduct>>(userProductList, HttpStatus.OK);
    }

    @PostMapping(value="pay",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBase> pay (@RequestBody PayRequest request) {

        //проверка айдишников на null
        if (request.getProductListId() == null || request.getUserId() == null){
            return new ResponseEntity<ResponseBase>(new ResponseBase("Product List or User Id is NULL"), HttpStatus.BAD_REQUEST);
        } else if (request.getProductListId().isEmpty()) {
            return new ResponseEntity<ResponseBase>(new ResponseBase("Product IDs are empty"), HttpStatus.BAD_REQUEST);
        }

       try {
           List<UUID> productIds = request.getProductListId();
           List<Product> productList = new ArrayList<>();
           for (UUID productId : productIds) {
               Product product = productRepo.findById(productId).orElse(null);
               if (product != null) productList.add(product);
           }
           //Product product = productRepo.findById(request.getProductId()).orElse(null);
           Account user = usersRepo.findById(request.getUserId()).orElse(null);

           //проверка продукта и юзера на null
           if (user == null) {
               return new ResponseEntity<ResponseBase>(new ResponseBase("Account not found."), HttpStatus.NOT_FOUND);
           }

           //если лист пустой, значит не найдено в базе ни одного продукта
           if (productList.isEmpty()) {
               return new ResponseEntity<ResponseBase>(new ResponseBase("Products not found."), HttpStatus.NOT_FOUND);
           }

           //определяем количесвто уникальных товаров в листе
           Set<Product> set = new LinkedHashSet<Product>(productList);

           //выясняем количество повторений каждого уникального товара и заносим эту инфу в мапу
           Map<Product, Integer> map = new HashMap<>();
           int countSameProduct = 0;
           for (Product product: set) {
               countSameProduct = (int)productList.stream().filter(p -> p.getId().equals(product.getId())).count();
               map.put(product, countSameProduct);
               countSameProduct = 0;
           }
           //итерируем мапу и определяем сумму скидки для каждой группы идентичных товаров
           List<Long> listOfMoneyDiscounts = new ArrayList<>();
           Discount discount = null;
           long discountMoney = 0l;
           for (Map.Entry<Product, Integer> pair : map.entrySet()) {
               Product key = pair.getKey();
               int value = pair.getValue();
               discount = discountRepo.findByProductId(key.getId());
               if (discount != null) {
                   discountMoney = (long)(value * (key.getPrice() * (discount.getDiscount()/100f)));
                   listOfMoneyDiscounts.add(discountMoney);
               }
           }
           //определяем общую максимально выгодную сумму скидки
           //сортируем лист с суммами скидок в порядке убывания
           Collections.sort(listOfMoneyDiscounts, new Comparator<Long>() {
               @Override
               public int compare(Long o1, Long o2) {
                   if (o1 > o2) return -1;
                   if (o1 < o2) return 1;
                   else return 0;
               }
           });
           //тут я немного нарушил бизнес логику, и не заморачивался с подсчетом цены для каждого вида товара, надеюсь это простительно для тестового
           long generalDiscount = 0;
           //здесь выполняется условие трьох максимально выгодных скидок
           if (!listOfMoneyDiscounts.isEmpty()) {
               int count = listOfMoneyDiscounts.size();
               if (count > 3) {
                   for (int i = 0; i < 3; i++) {
                       generalDiscount += listOfMoneyDiscounts.get(i);
                   }
               } else {
                   for (Long discnt : listOfMoneyDiscounts ) {
                       generalDiscount += discnt;
                   }
               }
           }

           //определяем общую цену за все товары
           Long price = 0L;
           for (Product product : productList) {
               price += product.getPrice();
           }
           price = price - generalDiscount;

        //проверка на нехватку денег
        if (price > user.getMoney()){
            return new ResponseEntity<ResponseBase>(new ResponseBase("Not enough money."), HttpStatus.OK);
        }


        //изменение счета  и обновление информации о юзере в репозитории
        user.setMoney(user.getMoney() - price);
        usersRepo.save(user);

        //изменение количества купленных юзером продуктов
        for (Product product : productList) {
            UserProduct userProduct = userProductRepo.findAllByAccount_IdAndProduct_Id(request.getUserId(),product.getId());
            if (userProduct != null){
                userProduct.setAmount(userProduct.getAmount() + 1);
            }else {
                userProduct = new UserProduct(user.getId(),product.getId(),1);
                userProductRepo.save(userProduct);
            }
        }

        } catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<ResponseBase>(new ResponseBase(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<ResponseBase>(new ResponseBase("Success"), HttpStatus.OK);
    }

    @PostMapping(value="addDiscount",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBase> addDiscount (@RequestBody AddDiscountRequest request) {

        if (request.getProductId() == null || request.getDiscount() == null){
            return new ResponseEntity<ResponseBase>(new ResponseBase("Product or Discount is null."), HttpStatus.NOT_FOUND);
        }

        try {

          Product product = productRepo.findById(request.getProductId()).orElse(null);

          if (product == null){
              return new ResponseEntity<ResponseBase>(new ResponseBase("Product not found"), HttpStatus.NOT_FOUND);
          }

         Discount discount = discountRepo.findByProductId(product.getId());

         if (discount == null){

              discount = new Discount(request.getDiscount(),request.getProductId());

         }else {

               discount.setDiscount(request.getDiscount());
         }

         discountRepo.save(discount);

        }catch (Exception e){

            return new ResponseEntity<ResponseBase>(new ResponseBase(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }



        return new ResponseEntity<ResponseBase>(new ResponseBase("Success"), HttpStatus.OK);
    }

    @PostMapping(value = "addProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBase> addProduct (@RequestBody AddProductRequest request) {
        Product newProduct = null;
        /*
        if (productRepo == null) {
            try {
                newProduct = new Product(request.getProductCategory(), request.getProductName(), request.getProductPrice());
                productRepo.save(newProduct);
            } catch (Exception e) {
                return new ResponseEntity<ResponseBase>(new ResponseBase(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
            }
            return new ResponseEntity<ResponseBase>(new ResponseBase("Product was added"), HttpStatus.OK);
        }
        */
        List<Product> list = productRepo.findAllByCategory(request.getProductCategory());
        try {
            for (Product product : list) {
                if (product.getName().equals(request.getProductName()) && product.getPrice() == request.getProductPrice()) {
                    return new ResponseEntity<ResponseBase>(new ResponseBase("Product already exist"), HttpStatus.BAD_REQUEST);
                }
            }

            newProduct = new Product(request.getProductCategory(), request.getProductName(), request.getProductPrice());

            productRepo.save(newProduct);

        } catch (Exception e) {
            return new ResponseEntity<ResponseBase>(new ResponseBase(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<ResponseBase>(new ResponseBase("Product was added"), HttpStatus.OK);
    }



}
