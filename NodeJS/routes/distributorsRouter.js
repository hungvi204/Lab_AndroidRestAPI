var express = require('express');//express dùng để tạo api
var router = express.Router();

//Thêm model
const Distributors = require('../models/distributors')
const Fruit = require('../models/fruit')
const Upload = require('../config/common/upload');
const User = require('../models/users');
const Transporter = require('../config/common/mail');
//Api thêm distributor
router.post('/add-distributor', async (req, res) => {
    try {
        const data = req.body;// Lấy dữ liệu từ body
        const newDistributor = new Distributors({
            name: data.name
        });//Tạo mới đối tượng
        const result = await newDistributor.save();//Lưu vào database
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

// Api lấy danh sách distributor
router.get('/get-list-distributor', async (req, res) => {
    try {
        //lấy danh sách theo thứ tự mới nhất
        const data = await Distributors.find().sort({ name: -1 })
        if (data) {
            res.json({
                "status": 200,
                "messenger": "Thành công",
                "data": data,
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Không thành công",
                "data": [],
            })
        }
    
    } catch (error) {   
        console.log(error)
    }
})

module.exports = router;