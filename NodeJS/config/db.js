const mongoose = require('mongoose')
mongoose.set('strictQuery', true)
// Đối với database dùng compass
const local = 'mongodb://127.0.0.1:27017/MyDatabase'
// Đối với database dùng atlas (mongodb cloud)
const atlas = 'mongodb+srv://hungvi:Hungvi2210@cluster0.vmywnx7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0'
const connect = async () => {
    try {
        await mongoose.connect(local/*atlas*/, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
        })
        console.log('Connect database successfully')
    } catch (error) {
        console.log(error)
        console.log('Connect database failure')
    }
}

module.exports = {connect}