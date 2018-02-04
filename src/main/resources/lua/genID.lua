local key = KEYS[1]
local id = redis.call('get',key)
if(id == false)
then
    redis.call('set',key,1)
    return key.."0001"
else
    redis.call('set',key,id+1)
    return key..string.format('%04d',id + 1)
end