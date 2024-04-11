var express = require('express');//express dùng để tạo api
var router = express.Router();
//Thêm model
const Distributors = require('../models/distributors')
const Fruit = require('../models/fruit')
const Upload = require('../config/common/upload');
const User = require('../models/users');
const Transporter = require('../config/common/mail');
const JWT = require('jsonwebtoken');
const { handlebars } = require('hbs');
const SECRETKEY = "FPTPOLYTECHNIC"
//Api thêm fruit
router.post('/add-fruit', async (req, res) => {
    try {
        const data = req.body;// Lấy dữ liệu từ body
        const newfruit = new Fruit({
            name: data.name,
            quantity: data.quantity,
            price: data.price,
            status: data.status,
            image: data.image,
            description: data.description,
            id_distributor: data.id_distributor
        });//Tạo mới đối tượng
        const result = await newfruit.save();//Lưu vào database
        if (result) {
            //Nếu thêm thành công result !null trả về dữ liệu
            res.json({
                "status": 200,
                "messenger": "Thêm thành công",
                "data": result,
            })
        } else {
            //Nếu thêm không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Thêm không thành công",
                "data": [],
            })
        }
    } catch (error) {
        console.log(error)
    }
})
//Get danh sách Fruits
router.get('/get-list-fruits', async (req, res, next) => {
    try {
        //Kiểm tra token mỗi lần gọi api/get-list-fruit
        const authHeader = req.headers['authorization'];
        //Authorization thêm từ kháo `Bearer token` nên sẽ xử lí cắt chuỗi để lấy token
        const token = authHeader && authHeader.split(' ')[1];
        //Nếu không có token trả về lỗi
        if (token == null) return res.sendStatus(401);
        let payload;
        JWT.verify(token, SECRETKEY, (err, user) => {
            if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401);//Trả về lỗi nếu token hết hạn
            if (err) return res.sendStatus(403);
            payload = user;
        });//Kiểm tra token không đúng hoặc hết hạn
        console.log(payload);
        const data = await Fruit.find().populate('id_distributor')//Lấy dữ liệu từ database
        res.json({
            "status": 200,
            "messenger": "Danh sách fruit",
            "data": data,
        })//Trả về dữ liệu
    } catch (error) {
        console.log(error)
    }
})
//Get chi tiết fruits (truyền params id)
router.get('/get-detail-fruit/:id', async (req, res) => {
    //id: param 
    try {
        const { id } = req.params // lấy dữ liệu thông qua id trên url gọi là params
        const data = await Fruit.findById(id).populate('id_distributor')//Lấy dữ liệu từ database
        res.json({
            "status": 200,
            "messenger": "Danh sách fruit",
            "data": data,
        })
    } catch (error) {
        console.log(error)
    }
})
//*Get danh sách Fruits (danh sách trả về gồm: name, quantity, price, id_ditributor) 
//nằm trong khoảng giá (query giá cao nhất, giá thấp nhất) và sắp xếp theo quantity (giảm dần)
router.get('/get-list-fruits-in-price', async (req, res) => {
    //:id param
    try {
        const { price_start, price_end } = req.query // lấy dữ liệu thông qua :id trên url gọi là param
        const query = { price: { $gte: price_start, $lte: price_end } }//Tạo query
        //$gte >= , $gte >
        //$lte >= , $lte >
        //truyền câu điều kiện , và chỉ lấy các trường mong muốn
        const data = await Fruit.find(query, 'name quantity price id ditributor')
            .populate('id_distributor')
            .sort({ quantity: -1 })//giảm dần = -1, tăng dần = 1
            .skip(0)//bỏ qua số lượng row
            .limit(2)//lấy 2 sản phẩm
        res.json({
            "status": 200,
            "messenger": "Danh sách fruit",
            "data": data,
        })
    } catch (error) {
        console.log(error)
    }
})

//*Get danh sách Fruits (danh sách trả về gồm: name, quantity, price, id_ditributor) 
//có chữ cái bắt đầu tên là A hoặc X
router.get('/get-list-fruits-have-name-a-or-x', async (req, res) => {
    //:id param
    try {
        const query = {
            $or: [
                { name: { $regex: 'T' } },
                { name: { $regex: 'X' } },
            ]
        }
        //truyền câu điều kiện , và chỉ lấy các trường mong muốn
        const data = await Fruit.find(query, 'name quantity price id ditributor')
            .populate('id_distributor')

        res.json({
            "status": 200,
            "messenger": "Danh sách fruit",
            "data": data,
        })
    } catch (error) {
        console.log(error)
    }
})
//Api cập nhật fruit
router.put('/update-fruit-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params
        const data = req.body
        const updatefruit = await Fruit.findById(id)
        let result = null
        if (updatefruit) {
            updatefruit.name = data.name ?? updatefruit.name;
            updatefruit.quantity = data.quantity ?? updatefruit.quantity;
            updatefruit.price = data.price ?? updatefruit.price;
            updatefruit.status = data.status ?? updatefruit.status;
            updatefruit.image = data.image ?? updatefruit.image;
            updatefruit.description = data.description ?? updatefruit.description;
            updatefruit.id_distributor = data.id_distributor ?? updatefruit.id_distributor;
            result = await updatefruit.save()
        }
        //tạo một đối tượng mới
        //thêm vào database
        if (result) {
            //Nếu thành công result !null trả về dữ liệu 
            res.json({
                "status": 200,
                "messenger": "Cập nhật thành công",
                "data": result
            })
        } else {
            //Nếu không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Cập nhật không thành công",
                "data": []
            })
        }
    } catch (error) {
        console.log(error)
    }
})
//Api xóa fruit
router.delete('/delete-fruit-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params
        const result = await Fruit.findByIdAndDelete(id)
        if (result) {
            //Nếu thành công result !null trả về dữ liệu 
            res.json({
                "status": 200,
                "messenger": "Xóa thành công",
                "data": result
            })
        } else {
            //Nếu không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Xóa không thành công",
                "data": []
            })
        }
    } catch (error) {

    }
})

// Api thêm fruit với file image
router.post('/add-fruit-with-file-image', Upload.array('image', 5), async (req, res) => {
    //Upload.array('image',5) => up nhiều file tối đa là 5
    //upload.single('image') => up load 1 file
    try {
        const data = req.body; // Lấy dữ liệu từ body
        const { files } = req //files nếu upload nhiều, file nếu upload 1 file
        const urlsImage =
            files.map((file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`)
        //url hình ảnh sẽ được lưu dưới dạng: http://localhost:3000/upload/filename
        const newfruit = new Fruit({
            name: data.name,
            quantity: data.quantity,
            price: data.price,
            status: data.status,
            image: urlsImage, /* Thêm url hình */
            description: data.description,
            id_distributor: data.id_distributor
        }); //Tạo một đối tượng mới
        const result = await newfruit.save(); //Thêm vào database
        if (result) {// Nếu thêm thành công result !null trả về dữ liệu
            res.json({
                "status": 200,
                "messenger": "Thêm thành công",
                "data": result
            })
        } else {// Nếu thêm không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Lỗi, thêm không thành công",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
});
router.get('/get-page-fruit', async (req, res) => {
    //Auten
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
    console.log("token: ", token);
    if (token == null) return res.sendStatus(401);
    let payload;
    JWT.verify(token, SECRETKEY, (err, _payload) => {
        if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401);
        if (err) return res.sendStatus(403);
        payload = _payload;
    });
    let perPage = 6; // số lượng item trên 1 page
    let page = req.query.page || 1; // page hiện tại
    let skip = (perPage * page) - perPage; // phân trang
    let count = await Fruit.find().count(); // tổng số item
    //lọc theo tên
    const name = { "$regex": req.query.name ?? "", "$options": "i" };
    //lọc theo giá >= giá truyền vào
    const price = { $gte: req.query.price ?? 0 };
    //lọc sắp xếp theo giá
    // const sort = { price: req.query.sort ?? 1 };
    try {
        const data = await Fruit.find({ name, price })
            .populate('id_distributor')
            .skip(skip)
            .limit(perPage);
        res.json({
            "status": 200,
            "messenger": "Danh sách fruit",
            "data": {
                "data": data,
                "currentPage": Number(page),
                "totalPage": Math.ceil(count / perPage)
            },
        });
    } catch (error) {
        console.log(error);
    }
})

module.exports = router;