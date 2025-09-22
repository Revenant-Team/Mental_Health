import mongoose from "mongoose";

const connectDB = async ()=>{
    try{
        await mongoose.connect("mongodb+srv://bytebros7_db_user:RzTVWmifu5Ic9Dkx@sih.uhmxx8f.mongodb.net/MentalHealth");
        console.log("Database Connection Successfully!!");
    }
    catch(err){
        console.log(`Error : ${err}`);
    }
}

export default connectDB;