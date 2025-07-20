db.createUser({
    user: "foobar",
    pwd: "foobar",
    roles: [{
        role: "readWrite",
        db: "oms_bootcamp"
    }]
});
