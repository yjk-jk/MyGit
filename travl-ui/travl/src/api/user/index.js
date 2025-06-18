import request from '../request'


export const UserLogin = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data: data
  })
}

export const UserRegister = (data) => {
  return request({
    url: '/user/add',
    method: 'post',
    data: data  
 })
}


export const UserLoginOut = (data) => {
  return request({
    url: '/user/logout',
    method: 'post',
    data: data  
 })
}


