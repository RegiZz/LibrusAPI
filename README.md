
# LibrusApi



## Usage/Examples

First you need to host it on server and then you can use it like in this example:

```javascript
import axios from 'axios';

const handleLogin = async () => {
    try {
      const response = await axios.post('https://your-server-ip/api/librus/login', {
        login: login,
        password: password,
      });
      if (response.status === 200) {
        setToken(response.data);
        Alert.alert('Succesfully logged in');
      } else {
        Alert.alert('Login error');
      }
    } catch (error) {
      Alert.alert('Error', error.message);
    }
  };

  const getTimetable = async () => {
    if (!token) {
      Alert.alert('You must to login first!');
      return;
    }

    try {
      const response = await axios.get('https://your-server-ip/api/librus/timetables', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
            // Date examples
          from: '2024-01-01', 
          to: '2024-01-07',   
        },
      });

      if (response.status === 200) {
        setTimetable(response.data);
      } else {
        Alert.alert('Error while fetching the timetable');
      }
    } catch (error) {
      Alert.alert('Error', error.message);
    }
  };
```


## Badges



Add badges from somewhere like: [shields.io](https://shields.io/)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)


## Authors

- [@RegiZz](https://www.github.com/RegiZz)


