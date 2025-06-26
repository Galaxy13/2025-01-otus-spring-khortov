db = db.getSiblingDB('maindb');

db.createUser({
    user: 'user',
    pwd: '123',
    roles: [
        { role: "dbOwner", db: "maindb" },
        { role: 'readWrite', db: 'maindb' }
    ]
});