package services;

import dao.DaoFactory;
import dao.xml.XmlDaoFoodImpl;
import model.Category;
import model.Food;
import org.xml.sax.SAXException;
import view.View;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public class FoodServiceImpl implements FoodService, DaoFactory {

    private View view = new View();
    private XmlDaoFoodImpl xmlDaoFood;

    @Override
    public void updateFood() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String nameCategory = view.nameCategory();
        Category category = new Category(nameCategory);
        String nameFood = view.nameDishes();
        Food oldFood = new Food(nameFood, category.getName());
        String newNameOfFood = view.newNameForDish();
        Food newFood = new Food(newNameOfFood, category.getName());
        getDaoXml();
        xmlDaoFood.update(oldFood, newFood);
    }

    @Override
    public void removeFood() throws IOException, TransformerException, SAXException, ParserConfigurationException {
        String nameCategory = view.nameCategory();
        Category category = new Category(nameCategory);
        String nameFood = view.nameDishes();
        Food food = new Food(nameFood, category.getName());
        getDaoXml();
        xmlDaoFood.delete(food);
    }

    @Override
    public void getAllDishes() throws IOException, SAXException, ParserConfigurationException {
        String nameCategory = view.nameCategory();
        Category category = new Category(nameCategory);
        getDaoXml();
        List<Food> listOfFood = xmlDaoFood.getBy(category);
        view.showAllDishes(listOfFood);
    }

    @Override
    public void createFood() throws TransformerException, SAXException, ParserConfigurationException, IOException {
        String nameCategory = view.nameCategory();
        Category category = new Category(nameCategory);
        String nameFood = view.nameDishes();
        int price = view.priceOfDishes();
        Food food = new Food(nameFood, category.getName(), price);
        getDaoXml();
        xmlDaoFood.create(food);
    }

    @Override
    public void getDaoXml() {
        this.xmlDaoFood = new XmlDaoFoodImpl();
    }
}
