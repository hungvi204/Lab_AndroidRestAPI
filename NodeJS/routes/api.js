var express = require('express');//express dùng để tạo api
var router = express.Router();

//Thêm model
const Distributors = require('../models/distributors')
const Fruit = require('../models/fruit')
const Upload = require('../config/common/upload');
const User = require('../models/users');
const Transporter = require('../config/common/mail');

// Api đăng ký
router.post('/register-send-email', Upload.single('avatar'), async (req, res) => {
    try {
        const data = req.body;
        let avatarUrl = ""; // URL của avatar mặc định
        if (req.file) {
            avatarUrl = `${req.protocol}://${req.get("host")}/uploads/${req.file.filename}`;
        }
        const newUser = User({
            username: data.username,
            password: data.password,
            email: data.email,
            name: data.name,
            avatar: avatarUrl // Sử dụng URL của avatar mặc định hoặc URL được tạo từ ảnh upload
        })
        const result = await newUser.save()
        if (result) {
            //Gửi mail
            const mailOptions = {
                from: "vinhpd08351@fpt.edu.vn",
                to: result.email,
                subject: "Đăng ký thành công",
                text: "Cảm ơn bạn đã đăng ký",
            };
            await Transporter.sendMail(mailOptions);
            res.json({
                "status": 200,
                "messenger": "Thêm thành công",
                "data": result
            })
        } else {
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