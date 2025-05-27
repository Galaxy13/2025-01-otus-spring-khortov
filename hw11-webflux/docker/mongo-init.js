db = db.getSiblingDB('maindb');

db.createUser({
    user: 'user',
    pwd: '123',
    roles: [
        { role: 'readWrite', db: 'maindb' }
    ]
});