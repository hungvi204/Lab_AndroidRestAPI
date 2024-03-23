var express = require('express');//express dùng để tạo api
var router = express.Router();

//Thêm model
const Distributors = require('../models/distributors')
const Fruit = require('../models/fruit')
const Upload = require('../config/common/upload');
const User = require('../models/users');
const Transporter = require('../config/common/mail');

//Get danh sách Fruits
// router.get('/get-list-fruits', async (req, res, next) => {
//     //Kiểm tra token mỗi lần gọi api/get-list-fruit
//     const authHeader = req.headers['authorization'];
//     //Authorization thêm từ kháo `Bearer token` nên sẽ xử lí cắt chuỗi để lấy token
//     const token = authHeader && authHeader.split(' ')[1];
//     //Nếu không có token trả về lỗi
//     if (token == null) return res.sendStatus(401);
//     let payload;
//     JWT.verify(token, SECRETKEY, (err, user) => {
//         if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401);//Trả về lỗi nếu token hết hạn
//         if (err) return res.sendStatus(403);
//         payload = user;
//     });//Kiểm tra token không đúng hoặc hết hạn
//     console.log(payload);
//     try {
//         const data = await Fruit.find().populate('id_distributor')//Lấy dữ liệu từ database
//         res.json({
//             "status": 200,
//             "messenger": "Danh sách fruit",
//             "data": data,
//         })//Trả về dữ liệu
//     } catch (error) {
//         console.log(error)
//     }
// })

// Api đăng ký
router.post('/register-send-email', Upload.single('avatar'), async (req, res) => {
    try {
        const data = req.body;
        const { file } = req
        const newUser = User({
            username: data.username,
            password: data.password,
            email: data.email,
            name: data.name,
            avatar: `${req.protocol}://${req.get("host")}/uploads/${file.filename}`,
            //url avatar http://localhost:3000/uploads/filename
        })
        const result = await newUser.save()
        if (result) {  //Gửi mail
            const mailOptions = {
                from: "vinhpd08351@fpt.edu.vn", //email gửi đi
                to: result.email, // email nhận
                subject: "Đăng ký thành công", //subject
                text: "Cảm ơn bạn đã đăng ký", // nội dung mail
            };
            // Nếu thêm thành công result !null trả về dữ liệu
            await Transporter.sendMail(mailOptions); // gửi mail
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
})
//Api đăng nhập
const JWT = require('jsonwebtoken');
const SECRETKEY = "FPTPOLYTECHNIC"
router.post('/login', async (req, res) => {
    try {
        const { username, password } = req.body;
        const user = await User.findOne({ username, password })
        if (user) {
            //Token người dùng sẽ sử dụng gửi lên trên header mỗi lần muốn gọi api
            const token = JWT.sign({ id: user._id }, SECRETKEY, { expiresIn: '1h' });
            //Khi token hết hạn, người dùng sẽ call 1 api khác để lấy token mới
            //Lúc này người dùng sẽ truyền refreshToken lên để nhận về 1 cặp token,refreshToken mới
            //Nếu cả 2 token đều hết hạn người dùng sẽ phải thoát app và đăng nhập lại
            const refreshToken = JWT.sign({ id: user._id }, SECRETKEY, { expiresIn: '1d' })
            //expiresIn thời gian token
            res.json({
                "status": 200,
                "messenger": "Đăng nhập thành công",
                "data": user,
                "token": token,
                "refreshToken": refreshToken
            })
        } else {
            // Nếu thêm không thành công result null, thông báo không thành công
            res.json({
                "status": 400,
                "messenger": "Lỗi, đăng nhập không thành công",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

module.exports = router;