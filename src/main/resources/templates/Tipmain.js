// Tipmain.js
import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from 'axios';
import "./Tipmain.css";


const axiosInstance = axios.create({
    baseURL: 'https://stguidebook-649b3dde2d26.herokuapp.com/tip_board', // 서버의 기본 URL로 수정
    timeout: 10000, // 요청 타임아웃 설정 (ms)
});

// 페이지 네비게이션 컴포넌트
const Pagination = ({ postsPerPage, totalPosts, currentPage, setCurrentPage }) => {
    const pageNumbers = [];

    const totalPages = Math.ceil(totalPosts / postsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        pageNumbers.push(i);
    }

    if (totalPages <= 1) return null; // 페이지가 1개 이하이면 네비게이션 숨김

    const handleClick = (number) => {
        setCurrentPage(number);
        window.scrollTo(0, 0); // 페이지 이동 시 스크롤을 상단으로 이동
    };

    return (
        <div>
            <ul className="pagination">
                {pageNumbers.map(number => (
                    <li key={number} className="pagination-item">
                        <button
                            onClick={() => {handleClick(number)}}
                            className={`pagination-button ${number === currentPage ? 'active' : ''}`}
                        >
                            {number}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

// 게시글 목록 컴포넌트
const Nav = ({ topics, onChangeMode }) => {
    return (
        <div>
            <ol className="tip-list">
                {topics.map(t => (
                    <li key={t.id} className="tip-list-item">
                        <p className="tip-item-id">{t.id}</p>
                        <Link
                            id={t.id}
                            to={`/tip/read/${t.id}`} // 상세 페이지 경로 수정
                            className="tip-item-title"
                            onClick={event => {
                                onChangeMode(Number(event.target.id));
                            }}
                        >
                            {t.title}
                        </Link>
                        <p className="tip-item-date">{t.date}</p>
                        <p className="tip-item-heart">{t.heart}</p>
                    </li>
                ))}
            </ol>
        </div>
    );
};

// 게시글 생성 컴포넌트
const Create = ({ onCreate }) => {
    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');

    return (
        <div className="create-wrap">
            <div className="tip-box-up">
                <h3>팁게시판</h3>
                <div className="input-wrap">
                    <input className="input" type="text" placeholder="글 제목, 내용" />
                    <img src="assets/img/tip/search.svg" alt="검색" />
                </div>
            </div>
            <div className="tip-box">
                <form onSubmit={event => {
                    event.preventDefault();
                    if (!title.trim() || !body.trim()) {
                        return; // 제출 중단
                    }
                    onCreate(title, body);
                    setTitle('');  // 제목을 비움
                    setBody('');   // 내용을 비움
                }}>
                    <input
                        className="input tip-title"
                        type="text"
                        name="title"
                        value={title}
                        placeholder="제목"
                        onChange={event => setTitle(event.target.value)}
                    />
                    <hr />
                    <textarea
                        className="input tip-body"
                        name="body"
                        value={body}
                        onChange={event => setBody(event.target.value)}
                        placeholder="글 내용을 입력해주세요."
                        cols="30"
                        rows="10"
                    ></textarea>
                    <input className="input tip-submit" type="submit" value="" />
                </form>
            </div>
        </div>
    );
};

// 메인 게시판 컴포넌트
const Tipmain = () => {
    const [id, setId] = useState(null);
    const [nextId, setNextId] = useState(4); // 서버에서 자동으로 ID를 관리할 경우 필요 없을 수 있음
    const [topics, setTopics] = useState([]);
    const [heartN, setHeart] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const postsPerPage = 5;
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [sortOrder, setSortOrder] = useState('date'); // 'date' 또는 'heart'

    const today = new Date();
    const formattedDate = `${today.getFullYear()}.${String(today.getMonth() + 1).padStart(2, '0')}.${String(today.getDate()).padStart(2, '0')}`;

    // 데이터 가져오기 (GET 요청)
    useEffect(() => {
        const fetchTopics = async () => {
            setLoading(true);
            try {
                const response = await axiosInstance.get('/list'); // Axios 인스턴스 사용
                console.log(response);
                // 서버에서 받은 데이터를 클라이언트 형식으로 변환
                const transformedTopics = response.data.map((item, index) => ({
                    id: nextId + index, // 클라이언트에서 id 부여
                    title: item.subject,
                    body: item.content,
                    date: new Date(item.createDate).toLocaleDateString('ko-KR'), // 날짜 형식 변환
                    heart: item.likeCount
                }));
                setTopics(transformedTopics);
                setNextId(nextId + transformedTopics.length);
            } catch (err) {
                console.error(err);
                setError('게시글을 불러오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchTopics();
    }, [nextId]);

    // 게시글 추가 함수 (POST 요청)
    const handleCreate = async (_title, _body) => {
        const newTopic = { title: _title, body: _body, date: formattedDate, heart: heartN };
        try {
            const response = await axiosInstance.post('/create', newTopic); // Axios 인스턴스 사용
            const createdTopic = {
                id: nextId,
                title: response.data.subject, // 서버에서 반환한 데이터가 있다면 매핑
                body: response.data.content,
                date: new Date(response.data.createDate).toLocaleDateString('ko-KR'),
                heart: response.data.likeCount
            };
            setTopics([createdTopic, ...topics]);
            setId(createdTopic.id);
            setNextId(nextId + 1);
            setHeart(0);
            setCurrentPage(1); // 새 게시글 추가 시 첫 페이지로 이동
        } catch (err) {
            console.error(err);
            setError('게시글을 추가하는 데 실패했습니다.');
        }
    };

    // 게시글 정렬 함수
    const sortedTopics = [...topics].sort((a, b) => {
        if (sortOrder === 'date') {
            return new Date(b.date) - new Date(a.date); // 최신순
        } else if (sortOrder === 'heart') {
            return Number(b.heart) - Number(a.heart); // 하트순
        }
        return 0;
    });

    // 현재 페이지에 표시할 게시글 계산 (정렬된 데이터 기준)
    const indexOfLastPost = currentPage * postsPerPage;
    const indexOfFirstPost = indexOfLastPost - postsPerPage;
    const currentPosts = sortedTopics.slice(indexOfFirstPost, indexOfLastPost);

    return (
        <div className="main">
            <h3>팁게시판</h3>
            <h1>여러분의 팁을 공유해주세요!</h1>
            <Create onCreate={handleCreate} />
            <div className="tip-menu-select">
                <div
                    className={`menu-date-select ${sortOrder === 'date' ? 'active' : ''}`}
                    onClick={() => setSortOrder('date')}
                >
                    최신순
                </div>
                <div
                    className={`menu-heart-select ${sortOrder === 'heart' ? 'active' : ''}`}
                    onClick={() => setSortOrder('heart')}
                >
                    하트순
                </div>
            </div>
            <div className="tip-menu-intro">
                <div className="tip-intro-id">번호</div>
                <div className="tip-intro-title">제목</div>
                <div className="tip-intro-date">등록일</div>
                <div className="tip-intro-heart"></div>
            </div>
            {loading ? (
                <p>로딩 중...</p>
            ) : error ? (
                <p>{error}</p>
            ) : (
                <Nav
                    topics={currentPosts}
                    onChangeMode={(id) => setId(id)}
                />
            )}
            <Pagination
                postsPerPage={postsPerPage}
                totalPosts={topics.length}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
            />
        </div>
    );
}

export default Tipmain;