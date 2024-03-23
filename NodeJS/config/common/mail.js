var nodemailer = require('nodemailer');
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'hungvinguyen424@gmail.com',
        pass: 'ngok fyau ufmn imjm'
    }
})

module.exports = transporter; //export ra ngoài để sử dụng