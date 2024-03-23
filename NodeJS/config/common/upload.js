const multer = require('multer');
const _storege = multer.diskStorage({ //lưu trữ file
    destination: function (req, file, cb) {
        cb(null, 'public/uploads')
    },
    filename: function (req, file, cb) {
        cb(null,file.fieldname + "-" + Date.now() + file.originalname)
    }
});
const upload = multer({ storage: _storege }); 
module.exports = upload; //export ra ngoài để sử dụng