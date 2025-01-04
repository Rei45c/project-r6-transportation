-- Insert the manager

INSERT INTO user (id, name, email, password, user_role)
SELECT 1, 'manager_name', 'm.m@gmail.com', 'm', 'MANAGER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'm.m@gmail.com'
);

-- Insert some vehicles

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 1, 'Salzburg, 5020, Austria', 1, 47.7981346, 13.0464806, 30, 3000, 'Truck'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 30
      AND max_weight = 3000 
      AND vehicle_type = 'Truck'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 2, 'Braunau am Inn, Bezirk Braunau, Upper Austria, 5280, Austria', 1, 48.253136, 13.039506, 50, 6000, 'Truck'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 50
      AND max_weight = 6000 
      AND vehicle_type = 'Truck'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 3, 'Vienna, Austria', 1, 48.2083537, 16.3725042, 40, 3000, 'Truck'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 40
      AND max_weight = 3000 
      AND vehicle_type = 'Truck'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 4, 'Linz, Upper Austria, Austria', 1, 48.3059078, 14.286198, 1, 500, 'Car'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 1
      AND max_weight = 500 
      AND vehicle_type = 'Car'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 5, 'St. Pölten, Lower Austria, Austria', 1, 48.2043985, 15.6229118, 2, 700, 'Car'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 2
      AND max_weight = 700 
      AND vehicle_type = 'Car'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 6, 'Wels, Upper Austria, 4600, Austria', 1, 48.1565472, 14.0243752, 0.6, 600, 'Car'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 0.6
      AND max_weight = 600 
      AND vehicle_type = 'Car'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 7, 'Innsbruck, Tyrol, Austria', 1, 47.2654296, 11.3927685, 8, 1500, 'Van'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 8
      AND max_weight = 1500 
      AND vehicle_type = 'Van'
);

INSERT INTO vehicle (id, address, available_vehicle, position_latitude, position_longitude, max_volume, max_weight, vehicle_type)
SELECT 8, 'Traun, Upper Austria, Austria', 1, 47.9484799, 13.815004, 12, 1300, 'Van'
WHERE NOT EXISTS (
    SELECT 1
    FROM vehicle 
    WHERE max_volume = 12
      AND max_weight = 1300 
      AND vehicle_type = 'Van'
);

-- Insert some drivers

INSERT INTO user (id, name, email, password, user_role)
SELECT 2, 'driver2', 'driver2.d@gmail.com', 'd2', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver2.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 2, 'Salzburg, 5020, Austria', 1, 47.7981346, 13.0464806, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 2
);
--
INSERT INTO user (id, name, email, password, user_role)
SELECT 3, 'driver3', 'driver3.d@gmail.com', 'd3', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver3.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 3, 'Vienna, Austria', 1, 48.2083537, 16.3725042, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 3
);
--
INSERT INTO user (id, name, email, password, user_role)
SELECT 4, 'driver4', 'driver4.d@gmail.com', 'd4', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver4.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 4, 'Linz, Upper Austria, Austria', 1, 48.3059078, 14.286198, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 4
);
--
INSERT INTO user (id, name, email, password, user_role)
SELECT 5, 'driver5', 'driver5.d@gmail.com', 'd5', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver5.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 5, 'Seefeld in Tirol, Bezirk Innsbruck-Land, Tyrol, 6100, Austria', 1, 47.3289241, 11.1867187, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 5
);
--
INSERT INTO user (id, name, email, password, user_role)
SELECT 6, 'driver6', 'driver6.d@gmail.com', 'd6', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver6.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 6, 'Traunstein, Gmunden, Bezirk Gmunden, Upper Austria, 4810, Austria', 1, 47.8731972, 13.8398047, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 6
);
--
INSERT INTO user (id, name, email, password, user_role)
SELECT 7, 'driver7', 'driver7.d@gmail.com', 'd7', 'DRIVER'
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE email = 'driver7.d@gmail.com'
);

INSERT INTO driver (id, address, available_driver, position_latitude, position_longitude, vehicle_id)
SELECT 7, 'Ried im Innkreis, Bezirk Ried, Upper Austria, 4910, Austria', 1, 48.2085868, 13.48839, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM driver
    WHERE id = 7
);