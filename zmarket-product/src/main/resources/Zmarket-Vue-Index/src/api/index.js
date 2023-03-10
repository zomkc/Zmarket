import axios from "axios";
//验证码
export function IndexPage() {
    // return axios.get( '/api/product/page')
    return axios.get( 'http://localhost:88/api/product/page')
}