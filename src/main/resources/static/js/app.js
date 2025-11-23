var main = {
    init: function () {
        var _this = this;
        _this.loadUser();

        // Index page logic
        if ($('#tbody').length > 0) {
            _this.loadPosts();
        }

        // Save page logic
        $('#btn-save').on('click', function () {
            _this.save();
        });

        // Update page logic
        var id = new URLSearchParams(window.location.search).get('id');
        if (id && $('#id').length > 0) {
            _this.loadPost(id);
        }

        $('#btn-update').on('click', function () {
            _this.update(id);
        });

        $('#btn-delete').on('click', function () {
            _this.delete(id);
        });
    },
    loadUser: function () {
        $.ajax({
            type: 'GET',
            url: '/api/v1/user',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function (user) {
            if (user) {
                $('#user-name').text(user.name);
                $('#user-info').show();
                $('#login-buttons').hide();
            } else {
                $('#user-info').hide();
                $('#login-buttons').show();
            }
        }).fail(function (error) {
            $('#user-info').hide();
            $('#login-buttons').show();
        });
    },
    loadPosts: function () {
        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/list',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function (posts) {
            var html = '';
            posts.forEach(function (post) {
                html += '<tr>';
                html += '<td>' + post.id + '</td>';
                html += '<td><a href="/posts-update.html?id=' + post.id + '">' + post.title + '</a></td>';
                html += '<td>' + post.author + '</td>';
                html += '<td>' + post.modifiedDate + '</td>';
                html += '</tr>';
            });
            $('#tbody').html(html);
        }).fail(function (error) {
            console.log(error);
        });
    },
    loadPost: function (id) {
        $.ajax({
            type: 'GET',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function (post) {
            $('#id').val(post.id);
            $('#title').val(post.title);
            $('#author').val(post.author);
            $('#content').val(post.content);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    save: function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            if (error.status === 401) {
                alert('로그인이 필요합니다.');
                window.location.href = '/';
            } else {
                alert(JSON.stringify(error));
            }
        });
    },
    update: function (id) {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            if (error.status === 401) {
                alert('로그인이 필요합니다.');
                window.location.href = '/';
            } else {
                alert(JSON.stringify(error));
            }
        });
    },
    delete: function (id) {
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            if (error.status === 401) {
                alert('로그인이 필요합니다.');
                window.location.href = '/';
            } else {
                alert(JSON.stringify(error));
            }
        });
    }
};

main.init();
