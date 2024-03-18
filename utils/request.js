import axios from 'axios'
import router from "@/router";

const request = axios.create({
    baseURL: 'http://localhost:9090',
    timeout: 5000
})

// request interceptor

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user) {
        config.headers['token'] = user.token;  // set request headers
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response interceptor

request.interceptors.response.use(
    response => {
        let res = response.data;
        // if return file
        if (response.config.responseType === 'blob') {
            return res
        }
        // string
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // when permission validation fails, display prompt
        if (res.code === '401') {
            // ElementUI.Message({
            //     message: res.msg,
            //     type: 'error'
            // });
            router.push("/login")
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

