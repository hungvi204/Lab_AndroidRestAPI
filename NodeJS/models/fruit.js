const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const Fruit = new Scheme({
    name: {type: String},
    quantity: {type: Number},
    price: {type: Number},
    status: {type: Number}, //status = 1 => Còn hàng, 0 => Hết hàng, -1 => Ngừng kinh doanh
    image: {type: Array}, //kiểu dữ liệu danh sách
    description: {type: String},
    id_distributor: {type: Scheme.Types.ObjectId, ref: 'distributors'}
},{
    timestamps: true
})

module.exports = mongoose.model('fruit', Fruit)

//type: Scheme.Types.ObjectId => kiểu dữ liệu id của mogodb
// ref: khóa ngoại