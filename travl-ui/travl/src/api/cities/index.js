import request from '../request';

// 获取所有城市
export const getAllCities = () => {
    return request({
        url: '/cities/getAll',
        method: 'get'
    });
};

// 获取分页城市列表
export const getAllCitiesByPage = (params) => {
    return request({
        url: '/cities/getAllByPage',
        method: 'post',
        params: params
    });
};

// 添加城市
export const addCity = (data) => {
    return request({
        url: '/cities/add',
        method: 'post',
        data: data
    });
};

// 根据 ID 获取城市
export const getCityById = (id) => {
    return request({
        url: `/cities/get/${id}`,
        method: 'get'
    });
};

// 更新城市
export const updateCity = (data) => {
    return request({
        url: `/cities/update/${data.id}`,
        method: 'put',
        data: data
    });
};

// 删除城市
export const deleteCity = (id) => {
    return request({
        url: `/cities/delete/${id}`,
        method: 'delete'
    });
};

// 上传城市图片
export const uploadCityImage = (cityId, file) => {
    const formData = new FormData();
    formData.append('file', file);

    return request({
        url: `/cities/${cityId}/upload`,
        method: 'post',
        data: formData, // 直接使用 formData
        headers: {
            'Content-Type': 'multipart/form-data' // 设置正确的 Content-Type
        }
    });
};


// 下载城市图片
export const downloadCityImage = (cityId) => {
    return request({
        url: `/cities/${cityId}/images`,
        method: 'get',
        responseType: 'blob' // 用于下载文件
    });
};