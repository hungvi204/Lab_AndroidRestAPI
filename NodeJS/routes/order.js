var express = require('express');//express dùng để tạo api
var router = express.Router();
//Thêm model
const Orders = require('../models/order')

router.post('/add-order', async (req, res) => {
    try {
        const data = req.body;
        const newOrder = new Orders({
            order_code: data.order_code,
            id_user: data.id_user
        })
        const result = await newOrder.save();//Lưu vào database
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Thêm thành công",
                "data": result,
            })
        } else{
            res.json({
                "status": 400,
                "messenger": "Thêm không thành công",
                "data": null,
            })
        }

    } catch (error) {
        console.log(error)
    }
})

module.exports = router;