create table if not exists Users(
    id int primary key,
    username varchar(50),
    password varchar(255),
    fullname varchar(100),
    contact varchar(50),
    email varchar(50),
    city varchar(50),
    state varchar(15),
    role varchar(15)
);

create table if not exists Rides(
    id int primary key,
    name varchar(30) not null,
    campus varchar(15) not null,
    contact varchar(30) not null,
    classTime int not null,
    seats int not null,
    carModel varchar(30) not null,
    carPhoto MEDIUMBLOB not null,
    address varchar(40) not null,
    userId int,
    foreign key(userId) references Users(id)
);
create table if not exists Passengers(
    id int primary key,
    passFullName varchar(30),
    numPassenger int,
    contact varchar(11),
    address varchar(40),
    foreign key(id) references Rides(id)
);

create table if not exists Tokens(
    id identity,
    userId integer,
    token varchar(255),
    foreign key(userId) references Users(id)
);

create table if not exists EffectRide(
    rideId identity,
    passName varchar(25),
    rideContact varchar(10),
    rideAddress varchar(40),
    foreign key (invent) reference Item(id)
);