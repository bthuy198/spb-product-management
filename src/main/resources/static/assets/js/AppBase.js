class AppBase {
    static DOMAIN_SERVER = location.origin;

    static API_SERVER = this.DOMAIN_SERVER + '/api';

    static API_AUTH = this.API_SERVER + "/auth";

    static API_PRODUCT = this.API_SERVER + "/products";

    static API_CATEGORY = this.API_PRODUCT + "/category";

    static API_CREATE_PRODUCT = this.API_PRODUCT + "/create";

    static API_UPDATE_PRODUCT = this.API_PRODUCT + "/update";

    static API_DELETE_PRODUCT = this.API_PRODUCT + "/delete";

    static API_CLOUDINARY = 'https://res.cloudinary.com/dank9jrti/image/upload';

    static SCALE_IMAGE_W_80_H_80_Q_100 = 'c_limit,w_80,h_80,q_100';
}
class ProductAvatar {
    constructor(id, fileFolder, fileName, fileUrl) {
        this.id = id;
        this.fileFolder = fileFolder;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

}

class Category{
    constructor(id, categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}

class Product{
    constructor(id, productAvatar, productName, price, quantity, description, category) {
        this.id = id;
        this.productAvatar = productAvatar;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
    }
}