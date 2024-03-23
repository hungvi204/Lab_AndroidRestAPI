const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const Users = new Scheme({
    username: { type: String, unique: true, maxlength: 255 },
    password: { type: String, maxlength: 255 },
    email: { type: String, unique: true },
    name: { type: String },
    avatar: { type: String },
    available: { type: Boolean, default: false },
}, {
    timestamps: true
})

module.exports = mongoose.model('user', Users)

/* 
mongoose.model('user', Users) 
đặt tên collection , đặt ở dạng số ít
thư viện moogoose sẽ tự động tạo ra tên collection ở dạng số nhiều (user => users)

Type: String Boolean => kiểu dữ liệu
unique: true => giá trị không được trùng
maxLength: 255 => độ dài tối đa của chuỗi
default: false => giá trị mặc định
timestamps: true => tự động thêm 2 trường createdAt và updatedAt
*/